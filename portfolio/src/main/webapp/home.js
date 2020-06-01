function getList() {
    console.log("in the function")
    fetch('/data').then(response => response.json()).then((list) => {
        document.getElementById('list_container').innerText = list.join('\n');
    });
}

async function getListAsyncAwait() {
  const response = await fetch('/data');
  const list = await response.json();
  console.log("fetching the list: ", list);
  document.getElementById('list_container').innerText = list;
}

function open_linkedin() {
    window.open("https://www.linkedin.com/in/juliareichel/");
}