function getGreeting() {
    console.log("in the function")
    fetch('/data').then(response => response.text()).then((greeting)=> {
        console.log("fetching the greeting")
        document.getElementById('greeting_container').innerText = greeting;
        console.log("should of printed!")
    });
}

async function getGreetingAsyncAwait() {
  const response = await fetch('/data');
  const quote = await response.text();
  document.getElementById('greeting_container').innerText = quote;
}

function open_linkedin() {
    window.open("https://www.linkedin.com/in/juliareichel/");
}