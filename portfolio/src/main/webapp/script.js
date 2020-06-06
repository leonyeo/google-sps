// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

window.onload = function() {
    // Find all elements to add the typing animation
    var elements = document.getElementsByClassName('txt-rotate');
    for (var i=0; i < elements.length; i++) {
        var text = elements[i].getAttribute('data-text');
        var time = elements[i].getAttribute('data-time');
        if (text) {
            txtRotate(elements[i], JSON.parse(text), parseInt(time), 0, true);
        } 
    }
    projectSlideNo = 0;
}

function txtRotate(elem, text, time, index, isAscending) {
    // Function to 'type' text, wait for time, backspace, then move on to next index in text array and repeat
    if (isAscending) {
        elem.innerHTML = text[index].slice(0, elem.innerHTML.length + 1);
        if (elem.innerHTML.length == text[index].length) {
            // If word has been displayed, pause for specified time, the backspace word
            setTimeout(txtRotate, time, elem, text, time, index, false);
            return;
        }
    }
    else if (!isAscending) {
        elem.innerHTML = text[index].slice(0, elem.innerHTML.length - 1);
        if (elem.innerHTML.length == 0) {
            // If word has been backspaced, move onto next index
            index = (index + 1) % text.length;
            setTimeout(txtRotate, 500, elem, text, time, index, true);
            return;
        }
    }
    // Else continue on to type/backspace next word
    setTimeout(txtRotate, 150, elem, text, time, index, isAscending);
}

var projectSlideNo;
function moveProject(i) {
    // Move forward or backward through the different projects, depending on if i is 1 or -1
    var elements = document.getElementsByClassName('project');
    elements[projectSlideNo].style.display = "none";
    if (projectSlideNo == 0 && i < 0) {
        projectSlideNo = elements.length - 1;
    }
    else projectSlideNo = (projectSlideNo + i) % elements.length;
    elements[projectSlideNo].style.display = "block";
}