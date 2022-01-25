from xhtml2pdf import pisa
from requests import get
from io import BytesIO

# https://stackoverflow.com/questions/4581789/how-do-i-get-user-ip-address-in-django
def get_client_ip(request):
    x_forwarded_for = request.META.get('HTTP_X_FORWARDED_FOR')
    if x_forwarded_for:
        ip = x_forwarded_for.split(',')[0]
    else:
        ip = request.META.get('REMOTE_ADDR')
    return ip

def pdf_render(path):
    BASE_URL = 'http://127.0.0.1:8000'
    url = f'{BASE_URL}{path}'
    content = get(url).content
    res = BytesIO()
    render = pisa.pisaDocument(
        content,
        res,
        path=url,
    )
    if render.err:
        raise Exception('pdf render error')
    return res.getvalue()