const FACTS =
  ['My middle name is Rose',
    'I was captain of my high school basketball & softball teams',
    'My favorite color is purple',
    'My favorite ice cream flavor is mint chocolate chip',
  ];

function addRandomFact() {
  const fact = FACTS[Math.floor(Math.random() * FACTS.length)];
  const factContainer = document.getElementById('fact_container');
  factContainer.innerText = fact;
}

function getPost() {
  document.getElementById('new_facts_container').innerHTML = "";
  fetch('/share-fact').then(response => response.json()).then((posts) => {
    posts.forEach((post) => createFactNode(post));
  });
}

function createFactNode(post) {
  const singlePostDiv = document.createElement('div');
  singlePostDiv.classList.add("post_div");
  singlePostDiv.innerText = (post.username + ": " + post.fact + "\n" + post.postTime);
  const replyButton = document.createElement('button');
  replyButton.innerText = "Reply to this Person";
  replyButton.classList.add("button_spacing");
  singlePostDiv.appendChild(replyButton);
  document.getElementById('new_facts_container').appendChild(singlePostDiv); 
  replyButton.addEventListener("click", () => promptReplyNode(singlePostDiv, replyButton));  
}

function promptReplyNode(singlePostDiv, button) {
  // console.log("PostId: " + postId);
  button.classList.add("hide");
  const replyForm = document.createElement('form');
  replyForm.method = 'POST';
  // replyForm.body = postId; 
  replyForm.action = '/reply';

  const replyInput = document.createElement('textarea');
  replyInput.name = "reply-input";
  replyForm.appendChild(replyInput);
  
  const submitReplyButton = document.createElement('button');
  submitReplyButton.innerText = "Reply";
  replyForm.appendChild(submitReplyButton);
  submitReplyButton.classList.add("button_spacing");
  singlePostDiv.appendChild(replyForm);
  // submitReplyButton.addEventListener("click",(event) => 
  //   fetch('/get-reply').then(response => response.json()).then((replies) => {
  //     replies.forEach((reply) => createReplyNode(reply, postDiv));
  //   });
  // )
  submitReplyButton.addEventListener("click", (event) => submitReply(event, replyInput, replyForm));
}

function submitReply(event, replyInput, form) {
  event.preventDefault();

  const replyObject = new URLSearchParams();
  replyObject.append("postId", postId);
  const replyData = replyInput.value; 
  replyObject.append("reply-input", replyData);
  
  const postRequest = new Request(form.action, {method: 'POST', body: replyObject, 
    headers: {"Content-Type": 'application/x-www-form-urlencoded'}
  });

  // fetch(postRequest).then(response => response.json()).then((replies) => {
  //   console.log("HERE!")
  //   // replies.forEach((reply) => createReplyNode(reply));
  // });
  fetch(postRequest).then(response => console.log("RESPONSE JSON:" ,response));
  // fetch(postRequest).then(response => console.log(response.formData()).then((data) => {
  //       console.log(data);
  //   }));
}

// function createReplyNode(reply, siblgePostDiv) {
//   const singleReplyDiv = document.createElement('div');
//   singleReplyDiv.innerText = reply;
//   singlePostDiv.append(singleReplyDiv);
// }