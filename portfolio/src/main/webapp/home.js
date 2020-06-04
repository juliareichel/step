function getContact() {
    var quantity = getLimit();
    fetch('/data?quantity='+ quantity).then(response => response.json()).then((emails) => {
        // document.getElementById('email_list').innerText = emails.join('\n');

        emails.forEach(email => {
          var singleEmailDiv = document.createElement('div');
          singleEmailDiv.classList.add("email_div_spacing");
          singleEmailDiv.innerText = email;
          document.getElementById('email_list').appendChild(singleEmailDiv);
          var replyButton = document.createElement('button');
          replyButton.innerText = "Reply to this Person";
          replyButton.classList.add("button_spacing");
          singleEmailDiv.appendChild(replyButton);
          var replyForm = document.createElement('form');
          replyForm.method = 'POST';
          singleEmailDiv.appendChild(replyForm);

          replyButton.addEventListener("click", () => {
            replyButton.classList.add("hide");
            var replyInput = document.createElement('textarea');
            replyForm.appendChild(replyInput);
            var submitReplyButton = document.createElement('button');
            submitReplyButton.innerText = "Reply";
            replyForm.appendChild(submitReplyButton);
            submitReplyButton.classList.add("button_spacing");
          })
        })
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
    return value;
}

function deleteContact() {
  const request = new Request('/delete-data', {method: 'POST'});
  fetch(request).then(response => getContact());
}

function open_linkedin() {
    window.open("https://www.linkedin.com/in/juliareichel/");
}