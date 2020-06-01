function getContact() {
    console.log("in the function")
    fetch('/data').then(response => response.json()).then((emails) => {
        // document.getElementById('list_container').innerText = list.join('\n');
        document.getElementById('email_list').innerText = emails;
        console.log("Email: ", emails[0]);
        // contacts.innterText = 'Email: ' + emails;
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