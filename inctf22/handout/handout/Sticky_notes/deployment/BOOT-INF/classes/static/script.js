var notes;
fetch('/render')
  .then(response => response.text())
  .then(response => {
	notes=response;
	note_arr=notes.split(";");
	console.log(note_arr);
	for(i=0;i<note_arr.length-1;i++){
		var t=document.createElement("TEXTAREA");
		t.setAttribute("readonly", true);
		var t_node = document.createTextNode(note_arr[i]);
		t.appendChild(t_node);
		document.body.insertBefore(t,document.body.nextSibling);
		
	}
	});

function redirect(){
	window.location.href="/add_notes";
}