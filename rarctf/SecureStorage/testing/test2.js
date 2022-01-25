<script>window.onmessage = (e) => {let { origin, data } = e;alert(data);};</script>
<iframe src="http://localhost:4000" onload="this.contentWindow.alert(localStorage.message)"></iframe>