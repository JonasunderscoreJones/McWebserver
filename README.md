# McWebserver
This is the fabric port, the Forge version is available [here](https://github.com/J-onasJones/McWebserver-forge)

<a href="https://github.com/J-onasJones/McWeberver/blob/master/LICENSE"><img src="https://img.shields.io/github/license/J-onasJones/McWebserver?style=flat&color=900c3f" alt="License"></a>
<a href="https://discord.gg/V2EsuUVmWh"><img src="https://img.shields.io/discord/702180921234817135?color=5865f2&label=Discord&style=flat" alt="Discord"></a>
<a href="https://www.curseforge.com/minecraft/mc-mods/mcwebserver"><img src="https://cf.way2muchnoise.eu/full_649823.svg" alt="CF"></a>
<a href="https://modrinth.com/mod/mcwebserver"><img src="https://img.shields.io/modrinth/dt/mcwebserver?logo=modrinth&label=&style=flat&color=242629&labelColor=00AF5C&logoColor=white" alt="Modrinth"></a>
<a href="https://modrinth.com/mod/mcwebserver"><img src="https://img.shields.io/modrinth/game-versions/mcwebserver?logo=modrinth&color=242629&labelColor=00AF5C&logoColor=white"></a>
![GitHub all releases](https://img.shields.io/github/downloads/J-onasJones/McWebserver/total?label=GitHub%20downloads)

<a align="center"><img src="https://jonasjones.me/uploads/mod-badges/support-fabric.png" width="250px"><img src="https://jonasjones.me/uploads/mod-badges/support-quilt.png" width="250px"><img src="https://jonasjones.me/uploads/mod-badges/support-forge.png" width="250px"></a>



### About the mod
This mod runs a simple HTTP server alongside the minecraft server in seperate threads.
The mod allows You to host websites, share files and Server Backups directly from your server without the need of a seperate server setup. Adjust the port and design your website.

The http server has up to no performance impact on the server on idle and only slightly affects it for a short amount of time (usually a couple milliseconds) when a user visits the website or requests a file.

Currently no ssl encryption is implemented yet and therefore HTTPS isn't possible, but it's coming soon(TM).

### Warning
If used wrongly this mod can be a security risk for your server and all devices connected to the network that your server is in.

Do **only** put files into your webservers root directory that you want to be accessible to the internet. Those files are **public** to the entire world.

**Use at your own risk!**


<img src="https://raw.githubusercontent.com/modrinth/art/main/Branding/Badge/badge-light__184x72.png" width="250px">

### Setup

1. Head over to [modrinth](https://modrinth.com/mod/mcwebserver) or [curseforge](https://www.curseforge.com/minecraft/mc-mods/mcwebserver) and download the correct version of the mod into your mods folder.
2. Restart Your Minecraft Server and let the mod create the config file. The webserver will be offline by default.
3. In the config file, enable the webserver and adjust all settings if needed.
4. Add the following files to your Webservers root directory:
    - index.html (the homepage)
    - 404.html (the website that shows up if a request is sent for a file that doesn't exist on the server)
    - not_supported.html (this is page is very unlikely to show up at any time and isn't necessary)
5. Restart your Minecraft server and You're good to go!
