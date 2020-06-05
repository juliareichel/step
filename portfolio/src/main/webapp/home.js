function getContact() {
    var quantity = getLimit();
    fetch('/data?quantity='+ quantity).then(response => response.json()).then((posts) => {
        posts.forEach((post) => createEmailNode(post));
    });
}

function getLimit() {
    var value = document.getElementById("quantity_value").value;
    return value;
}

function createEmailNode(post) {
  console.log("Post: ", post);
  const singleEmailDiv = document.createElement('div');
  singleEmailDiv.classList.add("email_div_spacing");
  singleEmailDiv.innerText = post.email;
  document.getElementById('email_list').appendChild(singleEmailDiv);
  const replyButton = document.createElement('button');
  replyButton.innerText = "Reply to this Person";
  replyButton.classList.add("button_spacing");
  singleEmailDiv.appendChild(replyButton);
  replyButton.addEventListener("click", () => promptReplyNode(singleEmailDiv, replyButton, post.postId));  
}

function promptReplyNode(emailDiv, button, postId) {
  console.log("PostId: " + postId);
  button.classList.add("hide");

  const replyForm = document.createElement('form');
  replyForm.method = 'POST';
  replyForm.body = postId; 
  replyForm.action = '/reply?';
  emailDiv.appendChild(replyForm);

  const replyInput = document.createElement('textarea');
  replyInput.name = "reply-input";
  replyForm.appendChild(replyInput);
  
  const submitReplyButton = document.createElement('button');
  submitReplyButton.innerText = "Reply";
  replyForm.appendChild(submitReplyButton);
  submitReplyButton.classList.add("button_spacing");
  submitReplyButton.addEventListener("click", (event) => submitReply(event, replyInput, replyForm, postId));
}

function submitReply(event, replyInput, form, postId) {
  event.preventDefault();
  console.log("SUBMIT REPLY RUNNING");

  const replyObject = new URLSearchParams();
  replyObject.append("postId", postId);
  const replyData = replyInput.value; 
  replyObject.append("reply-input", replyData);
  
  const postRequest = new Request(form.action, {method: 'POST', body: replyObject, 
    headers: {"Content-Type": 'application/x-www-form-urlencoded'}
  });
  fetch(postRequest).then(response => response.json()).then((replies) => {
    console.log("HERE!")
    // replies.forEach((reply) => createReplyNode(reply));
  });
  // fetch('/reply').then(response => console.log("RESPONSE JSON:" ,response.json()));
}

function createReplyNode() {
  const singleReplyDiv = document.createElement('div');
  singleReplyDiv.innerText = reply;
}

//For Reference Purposes:
// async function getListAsyncAwait() {
//   const response = await fetch('/data');
//   const list = await response.json();
//   console.log("fetching the list: ", list);
//   document.getElementById('list_container').innerText = list;
// }

function deleteContact() {
  const request = new Request('/delete-data', {method: 'POST'});
  fetch(request).then(response => getContact());
}

function open_linkedin() {
    window.open("https://www.linkedin.com/in/juliareichel/");
}