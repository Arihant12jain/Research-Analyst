document.addEventListener('DOMContentLoaded',()=>{
    chrome.storage.local.get(['researchNotes'],function(result){
        if(result.researchNotes){
            document.getElementById('notes').value=result.researchNotes;
        }
    })
    document.getElementById('summarizeBtn').addEventListener('click',summarizeText)
});
async function summarizeText() {
    try {
        console.log("✅ Summarization started...");

        // Get the active tab
        const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
        console.log("🆔 Active Tab:", tab);

        // Execute script to get selected text
        const [{result}] = await chrome.scripting.executeScript({
            target: { tabId: tab.id },
            function: () => window.getSelection().toString()       
        });

        console.log("📌 Result Object:", result);

        // Check if result is valid
        if (!result) {
            console.warn("⚠ No text selected.");
            showResult('⚠ Please select some text first.');
            return;
        }

       
        // Send the selected text to the backend API
        const response = await fetch('http://localhost:3235/text/generate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ content: result,Operation:"summarize" })
        });

        const data = await response.json();
        console.log("📌 API Response:", data);

        // Show the summarized text
        showResult(data|| "⚠ No summary available.");

    } catch (error) {
        console.error('❌ Error summarizing text:', error);
        showResult('❌ An error occurred while summarizing.');
    }
}


function showResult(message) {
    const resultBox = document.getElementById('results');
    console.log(message.summary);
    if (resultBox) {
        resultBox.innerHTML = `<p>${message.summary}</p>`;
    } else {
        alert(message); // Fallback if UI element not found
    }
}
   
