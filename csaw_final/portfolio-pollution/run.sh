#!/usr/bin/env sh
docker build -t polluted_portfolio . \
&& docker run -it -p 8888:8888 --rm polluted_portfolio
