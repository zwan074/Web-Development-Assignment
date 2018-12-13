"use strict"

//script to generate side navigation content
function createSideNav(page) {
    if (page == "main") {
        document.write("<nav><ul>" +
            "<li><a target=_top href= Main.html >Home</a></li>" +
            "<li><a target=_top href= html/Variables.html >Variables</a></li>" +
            "<li><a target=_top href=html/DataTypes.html>Data Types</a></li>" +
            "<li><a target=_top href=html/Strings.html>Strings</a></li>" +
            "<li><a target=_top href=html/Conditions.html>Conditions</a></li>" +
            "<li><a target=_top href=html/Loops.html>Loops</a></li>" +
            "<li><a target=_top href=html/Functions.html>Functions</a></li></ul></nav>");



    }
    else {

        document.write("<nav><ul>" +
            "<li><a target=_top href= ../Main.html >Home</a></li>" +
            "<li><a target=_top href= Variables.html >Variables</a></li>" +
            "<li><a target=_top href=DataTypes.html>Data Types</a></li>" +
            "<li><a target=_top href=Strings.html>Strings</a></li>" +
            "<li><a target=_top href=Conditions.html>Conditions</a></li>" +
            "<li><a target=_top href=Loops.html>Loops</a></li>" +
            "<li><a target=_top href=Functions.html>Functions</a></li></ul></nav>");
    }

    

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

function VariblesCodeDemo1() {

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

function DataTypesCodeDemo5() {

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
     * Jim
     * Pea
     * Mia
    `;

    alert(guestList);

}

function StringsCodeDemo3() {

    alert("Error: Unexpected token ILLEGAL");

}


function StringsCodeDemo4() {

    let guestList = "Guests:\n * Jim\n * Pea\n * Mia";

    alert(guestList);

}

function StringsCodeDemo5() {

    alert("Hello\nWorld");
    alert(`Hello
World` );

}

function StringsCodeDemo6() {

    alert("Error: 0 is read only");


}


function StringsCodeDemo7() {

    let txt = 'Hi';

    txt = 'h' + txt[1];

    alert(txt);


}

function StringsCodeSolution1() {

    function ucFirst(txt) {
        if (!txt) return txt;

        return txt[0].toUpperCase() + txt.slice(1);
    }

    alert(ucFirst("jim"));

}


function StringsCodeSolution2() {

    function checkSpam(txt) {
        let lowerTxt = txt.toLowerCase();

        return lowerTxt.includes('aaa') || lowerTxt.includes('bbb');
    }

    alert(checkSpam('buy aaa now'));
    alert(checkSpam('free bbb'));
    alert(checkSpam("innocent Jim"));

}

//--demo code and tasks solution in Conditions Section
function ConditionsCodeDemo1() {

    let year = prompt('In which year was google founded', '');

    if (year == 1998) alert('Correct!');
}

function ConditionsCodeDemo2() {

    let year = prompt('In which year was google founded', '');

    if (year == 1998) {
        alert('Corrct!');
    } else {
        alert('Please do some research?');
    }
}

function ConditionsCodeDemo3() {

    let year = prompt('In which year was google founded', '');

    if (year < 1998) {
        alert('A little bit early...');
    } else if (year > 1998) {
        alert('A little bit late');
    } else {
        alert('Correct!');
    }
}

function ConditionsCodeSolution1() {

    if ("0") { alert('Hi'); }

}

function ConditionsCodeSolution2() {

    let value = prompt('Type a number', '');

    if (value > 0) {
        alert(true);
    } else if (value < 0) {
        alert(false);
    } else {
        alert('is 0');
    }

}

//--demo code and tasks solution in Loops Section
function LoopsCodeDemo1() {
    let i;
    for (i = 0; i < 5; i++) {
        alert(i);
    }
    

}

function LoopsCodeDemo2() {

    let student = { firstName: "Jim", lastName: "Dee", age: 20 };

    let x;
    for (x in student) {
        alert(student[x]);
    }



}

function LoopsCodeDemo3() {

    let i =0;
    while (i < 10) {
        alert("The number is " + i);
        i++;
    }


}

function LoopsCodeDemo4() {

    let i = 0;
    do {
        alert("The number is " + i);
        i++;
    }
    while (i < 10);
    
}

function LoopsSolution1() {
    let i = 0;
    while (i < 3) {
        alert(`number ${i}!`);
        i++;
    }


}

function LoopsSolution2() {
    for (let i = 2; i <= 10; i++) {
        if (i % 2 == 0) {
            alert(i);
        }
    }
    
}


//--demo code and tasks solution in Functions Section

function FunctionsDemo1() {

    let a = myFunction(2, 3); 
    alert(a);
    function myFunction(x, y) {
        return x + y; 
    }

    
}