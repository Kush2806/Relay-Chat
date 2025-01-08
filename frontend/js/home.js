// home.js - Handles session creation and matching

function makeid(length) {
    let result = '';
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const charactersLength = characters.length;
    let counter = 0;
    while (counter < length) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
      counter += 1;
    }
    return result;
}

function generateUniqueSessionId() {
    return 'user-' + makeid(4) + '-' + Date.now();
}

const UserId = generateUniqueSessionId();
console.log(UserId);
const apiGatewayUrl = "https://10.7.94.121:8080";  // API Gateway URL
const session_manegement = "https://10.7.94.121:8081"
const matching_service = "https://10.7.94.121:8082"

// Handle user clicking the "Start" button
document.getElementById("startMatch").addEventListener("click", function () {
    createSession();
});


// Create a new session
function createSession() {
    fetch(`${session_manegement}/api/sessions/create`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({sessionId:UserId})
    })
    .then(response => response.json())
    .then(data => {
        console.log("Session created with ID:", UserId);
        // After creating the session, start the matching process
        matchWithUser(UserId);
    })
    .catch(error => console.error('Error creating session:', error));
}

// Match with an active user
function matchWithUser(sessionId) {
    fetch(`${matching_service}/api/matching/match`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: sessionId })
    })
    .then(response => response.json())
    .then(data => {
        // Redirect to chat page or update UI to show the chat interface
        console.log(data);
        window.location.href = `/match.html?userId=${encodeURIComponent(UserId)}}`;  // Or update UI to show chat page
    })
    .catch(error => console.error('Error matching user:', error));
}
