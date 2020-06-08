const QUOTES =
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