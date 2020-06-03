function getContact() {
    var quantity = getLimit();
    fetch('/data?quantity='+ quantity).then(response => response.json()).then((emails) => {
        document.getElementById('email_list').innerText = emails.join('\n');
    });
}

//For Reference Purposes:
// async function getListAsyncAwait() {
//   const response = await fetch('/data');
//   const list = await response.json();
//   console.log("fetching the list: ", list);
//   document.getElementById('list_container').innerText = list;
// }

function getLimit() {
    var value = document.getElementById("quantity_value").value;
    console.log(value)
    return value;
}

function deleteContact() {
  const request = new Request('/delete-data', {method: 'POST'});
  fetch(request).then(response => getContact());
}

function open_linkedin() {
    window.open("https://www.linkedin.com/in/juliareichel/");
}