import multiprocessing
worker_class = "aiohttp.GunicornWebWorker"
workers = min(7, multiprocessing.cpu_count()*2 + 1)
bind = "0.0.0.0:5000"
timeout = 90
keepalive = 3600
