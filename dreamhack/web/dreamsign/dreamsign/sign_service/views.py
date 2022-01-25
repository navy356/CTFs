from django.shortcuts import render, redirect, get_object_or_404
from .models import Document, SignDocument, DreamUser, Sign
from .forms import UserCreationForm, DocumentForm, SignForm
from django.http import HttpResponse
from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from .util import pdf_render, get_client_ip
import base64

def index(request):
    return render(request, 'index.html')

def signup(request):
    if request.method == "POST":
        form = UserCreationForm(request.POST)
        if form.is_valid():
            form.save()
            username = form.cleaned_data.get('username')
            raw_password = form.cleaned_data.get('password1')
            user = authenticate(username=username, password=raw_password)
            login(request, user)
            return redirect('sign_service:index')
    else:
        form = UserCreationForm()
    return render(request, 'registration/signup.html', {'form': form})

def sign_view(request, sign_id):
    sign = get_object_or_404(Sign, pk=sign_id)
    return HttpResponse(sign.image, content_type="image/png")

@login_required
def mysign(request):
    msg = None
    if request.method == 'POST':
        form = SignForm(request.POST)
        if form.is_valid():
            request.user.signpath = form.cleaned_data.get('signpath')
            request.user.save()
    signs = Sign.objects.filter(user=request.user).order_by('-time')
    return render(request, 'mysign.html', {'signs': signs})

@login_required
def mysign_upload(request):
    try:
        img = request.POST['uploadfile']
        process_img = base64.b64decode(img[len('data:image/png;base64,'):])
    except Exception as e:
        return redirect('/mysign')
    sign = Sign(user=request.user, image=process_img)
    sign.save()
    request.user.signpath = f'/sign/{sign.id}'
    request.user.save()
    return redirect('/mysign')

def documents(request):
    docs = Document.objects.order_by('-time')
    return render(request, 'documents.html', {'documents': docs})

@login_required
def document(request):
    return render(request, 'document.html')

@login_required
def document_create(request):
    if request.method == 'POST':
        form = DocumentForm(request.POST)
        if form.is_valid():
            doc = form.save(commit=False)
            doc.user = request.user
            doc.save()
            return redirect('/document')
        else:
            pass
    form = DocumentForm()
    return render(request, 'document_create.html', {'form': form})

@login_required
def document_view(request, document_id):
    doc = get_object_or_404(Document, pk=document_id)
    signdocs = SignDocument.objects.filter(document=doc)

    if signdocs:
        signers = list(map(lambda x: x.user.username, list(signdocs)))
    else:
        signers = ''

    if request.method == 'POST':
        if not request.user.username in signers:  # check already sign user
            pdf = pdf_render(f'/document/{document_id}/render_internal/{request.user.id}')
            signdoc = SignDocument(user=request.user, document=doc, pdf=pdf)
            signdoc.save()
        return redirect(f'/document/{document_id}')

    return render(request, 'document_view.html', {'document': doc, 'signers': signers, 'signdocs': signdocs})

@login_required
def sign_document_view(request, document_id):
    signdoc = get_object_or_404(SignDocument, pk=document_id)
    if not request.user.id in [signdoc.user.id, signdoc.document.user.id]:
        return redirect('/document')
    return HttpResponse(signdoc.pdf, content_type="application/pdf")

@login_required
def document_render(request, document_id):
    doc = get_object_or_404(Document, pk=document_id)
    return render(request, 'document_render.html', {'document': doc})

def document_render_internal(request, document_id, user_id):
    if not get_client_ip(request) in ['127.0.0.1']:
        return redirect('/document')
    doc = get_object_or_404(Document, pk=document_id)
    user = get_object_or_404(DreamUser, pk=user_id)
    return render(request, 'document_render.html', {'document': doc, 'user': user})