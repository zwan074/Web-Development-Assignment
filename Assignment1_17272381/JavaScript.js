"use strict"

//script to generate side navigation content
function createSideNav() {
    document.write("<nav><ul>" +
        "<li><a target=_top href= Main.html >Home</a></li>" +
        "<li><a target=_top href= Variables.html >Variables</a></li>" +
        "<li><a target=_top href=DataTypes.html>Data Types</a></li>" +
        "<li><a target=_top href=Strings.html>Strings</a></li>" +
        "<li><a target=_top href=Conditions.html>Conditions</a></li>" +
        "<li><a target=_top href=Loops.html>Loops</a></li>" +
        "<li><a target=_top href=Functions.html>Functions</a></li></ul></nav>");

}

//script to generate feedback form at bottom to interact with visitors.
function createFeedBackForm() {
    document.write(
        "<hr /><form>" +
        "<h4>Please leave your enquiry below</h4>" +
        "<p><label>Your name is: </label><input type=text id=username required=required /></p>" +

        "<p><label>Your email address is: </label>" +
        "<input type=text id=emailadd required=required placeholder=\"e.g. abc@gmail.co.nz\" /></p>" +

        "<p><textarea id=message rows=10 cols=30>Please leave your feedback here</textarea></p>" +

        "<input type=submit value=Submit onclick=formValidation() /></form><br />");
    
}

//script to validate username , email, and feedback message input
function formValidation() {

    let uname = document.getElementById("username").value;
    let uemail = document.getElementById("emailadd").value;
    let umessage = document.getElementById("message").value;

    if (ValidateMessage(umessage)) {
        if (ValidateName(uname)) {
            if (ValidateEmail(uemail)) {
                alert("Feedback Sent");
            }
        }
    }


    return false;
}

// validate input in message box
function ValidateMessage(umessage) {
    let message = "Please leave your feedback here";
    if (umessage.match(message)) {
        alert('Please enter your message');
        uname.focus();
        return false;
    }
    else {
        return true;
    }
}

//validate input in name box
function ValidateName(uname) {
    let letters = /^[A-Za-z]+$/;//name format regex
    if (uname.match(letters)) {
        return true;
    }
    else if (uname.match("")) {

        alert('Please enter username');
        uname.focus();

    }

    else {
        alert('Username must have alphabet characters only');
        uname.focus();
        return false;
    }
}


//validate input in email box
function ValidateEmail(uemail) {
    let mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;//exmail format regex. 
    if (uemail.match(mailformat)) {
        return true;
    }

    else if (uemail.match("")) {

        alert('Please enter your email address');
        uname.focus();

    }

    else {
        alert("You have entered an invalid email address!");
        uemail.focus();
        return false;
    }
} 

//Display and hide content by clicking solution by using jQuerry. 
function SolutionAndAnswer(solutionNum, answerNum) {
    $(document).ready(function () {
        $(solutionNum).click(function () {
            $(answerNum).slideToggle("quick");
        });
    });

}

//--demo code and tasks solution in Varibles Section

function VariblesCodeDemo1 () {

    let message;
    message = 'Hello';

    alert(message);

}

function VariblesCodeDemo2() {

    let message = 'Hello!'; 

    alert(message); 

}

function VariblesCodeDemo3() {

    alert("Error: invalid assignment to const `myBirthday");

}

//--demo code and tasks solution in Data Types Section
function DataTypesCodeDemo_1_and_2() {

    alert("Infinity");

}

function DataTypesCodeDemo_3_and_4() {

    alert("NaN");

}

function DataTypesCodeDemo5 () {

    let name = "John";
    
    alert(`Hello, ${name}!`); 
    
    alert(`the result is ${1 + 2}`); 

}

function DataTypesCodeDemo6() {

    alert("the result is ${1 + 2}");

}

function DataTypesCodeDemo7() {


    alert(isGreater); 

}

//--demo code and tasks solution in Strings Section

function StringsCodeDemo1() {

    function sum(a, b) {
        return a + b;
    }

    alert(`1 + 2 = ${sum(1, 2)}.`); 

}

function StringsCodeDemo2() {

    let guestList = `Guests:
     * John
     * Pete
     * Mary
    `;

    alert(guestList); 

}

function StringsCodeDemo3() {

    alert("Error: \"\" string literal contains an unescaped line break");

}


function StringsCodeDemo4() {

    let guestList = "Guests:\n * John\n * Pete\n * Mary";

    alert(guestList); 

}

function StringsCodeDemo5() {

    alert("Hello\nWorld"); 
    alert(`Hello
World` );

}

function StringsCodeDemo6() {

    alert("Error: 0 is read - only");
    

}


function StringsCodeDemo7() {

    let str = 'Hi';

    str = 'h' + str[1];  

    alert(str); 


}

function StringsCodeSolution1() {

    function ucFirst(str) {
        if (!str) return str;

        return str[0].toUpperCase() + str.slice(1);
    }

    alert(ucFirst("john")); 

}


function StringsCodeSolution2() {

    function checkSpam(str) {
        let lowerStr = str.toLowerCase();

        return lowerStr.includes('viagra') || lowerStr.includes('xxx');
    }

    alert(checkSpam('buy ViAgRA now'));
    alert(checkSpam('free xxxxx'));
    alert(checkSpam("innocent rabbit"));

}

//--demo code and tasks solution in Conditions Section
function ConditionsCodeDemo1() {

    let year = prompt('In which year was ECMAScript-2015 specification published?', '');

    if (year == 2015) alert('You are right!');
}

function ConditionsCodeDemo2() {

    let year = prompt('In which year was ECMAScript-2015 specification published?', '');

    if (year == 2015) {
        alert('You guessed it right!');
    } else {
        alert('How can you be so wrong?'); 
    }
}

function ConditionsCodeDemo3() {

    let year = prompt('In which year was ECMAScript-2015 specification published?', '');

    if (year < 2015) {
        alert('Too early...');
    } else if (year > 2015) {
        alert('Too late');
    } else {
        alert('Exactly!');
    }
}

function ConditionsCodeSolution1() {

    if ("0") { alert('Hello'); } 

}

function ConditionsCodeSolution2() {

    let value = prompt('Type a number', 0);

    if (value > 0) {
        alert(1);
    } else if (value < 0) {
        alert(-1);
    } else {
        alert(0);
    }

}
