#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:5.0 AS base
WORKDIR /app
EXPOSE 80
EXPOSE 443

FROM mcr.microsoft.com/dotnet/sdk:5.0 AS build
WORKDIR /src
COPY ["FileShare/Server/FileShare.Server.csproj", "FileShare/Server/"]
COPY ["FileShare/Client/FileShare.Client.csproj", "FileShare/Client/"]
COPY ["FileShare/Shared/FileShare.Shared.csproj", "FileShare/Shared/"]
RUN dotnet restore "FileShare/Server/FileShare.Server.csproj"
COPY . .
WORKDIR "/src/FileShare/Server"
RUN dotnet build "FileShare.Server.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "FileShare.Server.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
ENV WAIT_VERSION 2.9.0
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait
COPY --from=publish /app/publish .
ENTRYPOINT ["sh", "-c", "/wait && dotnet FileShare.Server.dll"]