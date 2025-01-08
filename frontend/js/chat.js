// Configuration for WebRTC, including STUN and TURN servers
const configuration = {
    iceServers: [
        { urls: "stun:stun.l.google.com:19302" },
        { urls: "turn:10.7.94.121:3478", username: "root", credential: "root" }
    ]
};

const socket = new SockJS('https://10.7.94.121:8082/ws-match');  // WebSocket endpoint as defined in WebSocketConfig
const stompClient = Stomp.over(socket);

let id;

// Function to connect to WebSocket and subscribe to match notifications
function connectWebSocket(userId) {
    stompClient.connect({}, function (frame) {
        console.log('Connected to WebSocket:', frame);

        // Subscribe to the WebSocket topic for the current user
        stompClient.subscribe(`/topic/match/${userId}`, function (message) {
            id = message.body;
        });
    }, function (error) {
        console.error('WebSocket connection error:', error);
    });
}

function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}
const currentSessionId = getQueryParam('userId');


// Video elements
const localVideo = document.getElementById('localVideo');
const remoteVideo = document.getElementById('remoteVideo');

// Chat elements
const chatDisplay = document.getElementById('chat-display');
const messageInput = document.getElementById('message');
const sendMessageButton = document.getElementById('send-message');

// Footer buttons
const newMatchButton = document.getElementById('new-match');
const closeSessionButton = document.getElementById('close-session');

let localStream;
let peerConnection;
let dataChannel;

const apiGatewayUrl = "https://10.7.94.121:8080";
const session_manegement = "https://10.7.94.121:8081"
const matching_service = "https://10.7.94.121:8082"
const video_chat = "https://10.7.94.121:8083"
const termination_service = "https://10.7.94.121:8084"



// Get local media once and initialize WebRTC when the page loads
navigator.mediaDevices.getUserMedia({ video: true, audio: true })
    .then((stream) => {
        localVideo.srcObject = stream;
        localStream = stream;
        initWebRTC();  // Initialize WebRTC after getting the media stream
    })
    .catch(error => console.error('Error accessing media devices.', error));

// Initialize WebRTC connection
function initWebRTC() {
    peerConnection = new RTCPeerConnection(configuration);

    // Add local stream tracks to the connection
    localStream.getTracks().forEach(track => peerConnection.addTrack(track, localStream));

    // Create a data channel for chat
    dataChannel = peerConnection.createDataChannel("chat");
    dataChannel.onmessage = (event) => displayChatMessage(event.data, 'remote');

    // Handle ICE candidates
    peerConnection.onicecandidate = event => {
        if (event.candidate) {
            fetch(`${video_chat}/api/video/sendCandidate?sessionId=${id}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(event.candidate)
            }).then(response => response.json())
              .then(data => console.log('Candidate sent:', data))
              .catch(error => console.error('Error sending candidate:', error));
        }
    };

    // Handle remote video stream
    peerConnection.ontrack = (event) => {
        const [stream] = event.streams;
        remoteVideo.srcObject = stream;
    };

    // Handle negotiation needed for offer/answer exchange
    peerConnection.onnegotiationneeded = async () => {
        const offer = await peerConnection.createOffer();
        await peerConnection.setLocalDescription(offer);
        fetch(`${video_chat}/api/video/sendCandidate?sessionId=${id}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/text' },
            body: JSON.stringify(offer)
        }).then(response => response.json())
          .then(data => console.log('Offer sent:', data))
          .catch(error => console.error('Error sending offer:', error));
    };

    // Handle receiving an offer from peer
    peerConnection.ondatachannel = (event) => {
        const receiveChannel = event.channel;
        receiveChannel.onmessage = (event) => displayChatMessage(event.data, 'remote');
    };

    // Fetch and set the remote ICE candidates
    fetchRemoteIceCandidates();
}

// Fetch remote ICE candidates
function fetchRemoteIceCandidates() {
    fetch(`${video_chat}/api/video/receiveCandidate?sessionId=${id}`,{method: 'GET'})
        .then(response => response.json())
        .then(candidate => peerConnection.addIceCandidate(new RTCIceCandidate(candidate))
            .then(() => console.log('Remote candidate added successfully'))
            .catch(error => console.error('Error adding remote candidate:', error)))
        .catch(error => console.error('Error receiving candidate:', error));
}

// Function to send a chat message
sendMessageButton.addEventListener('click', () => {
    const message = messageInput.value.trim();
    if (message) {
        displayChatMessage(message, 'local');
        dataChannel.send(message);  // Send the message via the data channel
        messageInput.value = '';  // Clear the input box
    }
});

// Display chat messages
function displayChatMessage(message, sender) {
    const messageElement = document.createElement('div');
    messageElement.className = sender === 'remote' ? 'remote-message' : 'local-message';
    messageElement.textContent = message;
    chatDisplay.appendChild(messageElement);
    chatDisplay.scrollTop = chatDisplay.scrollHeight;  // Auto-scroll to the latest message
}

// Handle new match request
newMatchButton.addEventListener('click', () => {
    fetch(`${matching_service}/api/matching/newmatch`, { method: 'POST' , body: {"userId":currentSessionId , "id":id} })
        .then(response => {
            if (response.ok) {
                console.log('New match requested successfully');
                resetChatAndVideo();  // Reset chat and video sections for the new match
            } else {
                console.error('Error requesting new match');
            }
        });
});

// Reset chat and video sections for a new match
function resetChatAndVideo() {
    chatDisplay.innerHTML = '';  // Clear chat display

    if (peerConnection) {
        peerConnection.close();  // Close the existing connection
        initWebRTC();  // Reinitialize WebRTC for the new match
    }

    console.log('Chat and video reset for new match.');
}

// Handle session termination
closeSessionButton.addEventListener('click', () => {
    fetch(`${matching_service}/api/matching/close`, { method: 'POST' , body: {"userId":currentSessionId , "id":id} })
        .then(response => console.log('Session terminated in matching service'));

    fetch(`${session_manegement}/api/sessions/terminate`, { method: 'POST' , body: {"sessionId": currentSessionId} })
        .then(response => console.log('Session terminated in session management service'));

    window.close();  // Close the current browser tab
});

