function getContact() {
    var quantity = getLimit();
    fetch('/data?quantity='+ quantity).then(response => response.json()).then((emails) => {
        emails.forEach(email => {
          const singleEmailDiv = document.createElement('div');
          singleEmailDiv.classList.add("email_div_spacing");
          singleEmailDiv.innerText = email;
          document.getElementById('email_list').appendChild(singleEmailDiv);
          const replyButton = document.createElement('button');
          replyButton.innerText = "Reply to this Person";
          replyButton.classList.add("button_spacing");
          singleEmailDiv.appendChild(replyButton);
          const replyForm = document.createElement('form');
          replyForm.method = 'POST';
          replyForm.action = '/reply?replyId=${replyId}';
          singleEmailDiv.appendChild(replyForm);

          replyButton.addEventListener("click", () => {
            replyButton.classList.add("hide");
            const replyInput = document.createElement('textarea');
            replyInput.name = "reply-input";
            replyForm.appendChild(replyInput);
            const submitReplyButton = document.createElement('button');
            submitReplyButton.innerText = "Reply";
            replyForm.appendChild(submitReplyButton);
            submitReplyButton.classList.add("button_spacing");
              submitReplyButton.addEventListener("click", () => {
                fetch(replyForm.action).then(response => response.json().then((replies) => {
                  //add to proper original comment
                }))
              })
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