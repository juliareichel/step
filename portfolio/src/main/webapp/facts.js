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
}