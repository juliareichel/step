function getContact() {
    fetch('/data').then(response => response.json()).then((emails) => {
        document.getElementById('email_list').innerText = emails;
    });
}

// async function getListAsyncAwait() {
//   const response = await fetch('/data');
//   const list = await response.json();
//   console.log("fetching the list: ", list);
//   document.getElementById('list_container').innerText = list;
// }

function open_linkedin() {
    window.open("https://www.linkedin.com/in/juliareichel/");
}