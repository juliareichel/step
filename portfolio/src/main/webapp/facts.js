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
  replyButton.classList.add("reply_button");
  singlePostDiv.appendChild(replyButton);
  document.getElementById('new_facts_container').appendChild(singlePostDiv); 

  const replyDiv = createReplyNode(post.replies, singlePostDiv);
  replyButton.addEventListener("click", () => promptReplyNode(singlePostDiv, replyButton, post.postId));  
}

function promptReplyNode(singlePostDiv, button, postId) {
  button.classList.add("hide");
  const replyForm = document.createElement('form');
  replyForm.method = 'POST';
  replyForm.action = '/reply';

  const replyInput = document.createElement('textarea');
  replyInput.name = "reply-input";
  replyForm.appendChild(replyInput);

  var input = document.createElement('input');
    input.type = 'hidden';
    input.name = "postId"; 
    input.value = postId;
  replyForm.appendChild(input);
  
  const submitReplyButton = document.createElement('button');
  submitReplyButton.type = "submit";
  submitReplyButton.innerText = "Reply";
  replyForm.appendChild(submitReplyButton);
  submitReplyButton.classList.add("button_spacing");
  singlePostDiv.appendChild(replyForm);
}

function createReplyNode(replies, singlePostDiv) {
  replies.forEach((reply) => {
    const singleReplyDiv = document.createElement('div');
    singleReplyDiv.classList.add("reply_text");
    singleReplyDiv.innerText = (reply.username + ": " + reply.reply + "\n" + reply.postTime);
    singlePostDiv.append(singleReplyDiv);
  });
}

function deleteFacts() {
  const request = new Request('/delete-post', {method: 'POST'});
  fetch(request).then(response => getPost());
}