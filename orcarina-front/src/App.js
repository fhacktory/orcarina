import React from 'react';
import './App.scss';

let activeInput = 1;

function InputBox(props) {
  return <div id={props.id} className={'input ' + (activeInput === props.id ? 'passive':'active')}>-</div>;
}

function writeInput(input) {
  if (input.key > 0 && input.key < 5) {
    document.getElementById(activeInput).innerHTML = input.key;
    if (activeInput === 4) {
      document.body.className += ' success';
      document.getElementById(1).innerHTML = "-";
      document.getElementById(2).innerHTML = "-";
      document.getElementById(3).innerHTML = "-";
      document.getElementById(4).innerHTML = "-";
      activeInput = 1;
      return true;
    }
    activeInput = activeInput + 1;
  }
}

function App() {
  return (
    <div className="App" onKeyDown={writeInput}>
      {/* <div className="Message">
        Orcarina
        <div className="Status">Waiting for server</div>
      </div> */}

      <div className="inputs">
        <InputBox id="1"/>
        <InputBox id="2"/>
        <InputBox id="3"/>
        <InputBox id="4"/>
      </div>

    </div>
  );
}

export default App;