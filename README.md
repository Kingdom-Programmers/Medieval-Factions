# Description
An open source minecraft spigot plugin for simulating sovereign states. Features include Warfare, Territory, Power Mechanics, Faction Homes, Alliances, Locks, Vassal Mechanics, Laws, Gates, Duels, Dynmap Integration, Localization and more.

# FAQ
## What is this?
Medieval Factions is a system of mechanics that allows for the simulation of sovereign nations. Players can create nations, claim territory, engage in warfare or politics, write laws or hold dueling tournaments, and generally are able to attempt to recreate society somewhat.

## How do I make factions claim more land?
A faction is able to claim as much land as their cumulative power level allows. The cumulative power level is made up of the sum of the power levels of the members of the faction. This will allow you and your other members to gain more power through killing/scheduled increases which will increase your cumulative power level which is tied to how much land your faction can claim.

## How do I change a config setting?
Type /mf config show to view the config. Type /mf config set (option) (value) to change a config setting. After this, you may have to restart the server. This must be done in-game with these commands, or else the changes won't save upon a restart.

## How do I change the language of the plugin?
To change what language the plugin is using, modify the "languageid" config option to one of the supported languages found in the plugin information outputted when you type /mf with no arguments.

## How do I use a translation file that isn't supported?
To use your own translation for the plugin, rename the en-us.tsv file in the "languages" folder of the "MedievalFactions" folder. Open the file and translate the values for each key accordingly. Then, modify the "languageid" to be your file name without ".tsv". Your translated key values should immediately be used by the plugin.

## Locks aren't working on 1.12.2! What do I do?
This is a known issue, and you can check its progress here.

# Notes
- Project building works with Java 8. It may work with other versions but I know that it works with Java 8.
- This project uses the IntelliJ plugin Minecraft Development and Maven to compile its code.
- Testing can be done by running a local spigot server with the compiled JAR in the plugins folder and checking the console.

# Major Version Changes [X.Y]
- 0.1 Initial Build
- 0.2 'Faction Class' update
- 0.3 'Allow Faction Creation' update
- 0.4 'Allow Faction Deletion' update
- 0.5 'Help' update
- 0.6 'More Commands' update
- 0.7 'Inviting/Kicking/Leaving' update
- 0.8 'Manual Saving/Loading' update
- 0.9 'Ownership Transfer' update
- 1.0 'Message Improvements' update
- 1.1 'Player Versus Player' update
- 1.2 'Land Claiming' update
- 1.3 'File Deletion' update
- 1.4 'Faction-wide Messages' update
- 1.5 'Officers' update
- 1.6 'Power Levels' update
- 1.7 'Faction Homes' update
- 1.8 'Advanced Warfare' update
- 1.9 'More Commands' update
- 2.0 'Alliances' update
- 2.1 'Permissions' update
- 2.2 'Timers' update
- 2.3 'Autosave' update
- 2.4 'Rename' update
- 2.5 'Locks' update
- 2.6 'Laws' update
- 2.7 'Improved List Command' update
- 2.8 'Power Changes' update
- 2.9 'More Claimed Chunk Behavior' update
- 3.0 'Faction Chats' update
- 3.1 'Admin Bypass' update
- 3.2 'Config.yml' update
- 3.3 'Major Overhauls & Bug Fixes' update
- 3.4 'Config Command + Bug Fix/Tweaks' update
- 3.5 'Vassal Factions, Power Decay & Many Tweaks' update
- 3.6 'Gates, Duels, bStats, Force Command & Several Tweaks' update
- 4.0 'Dynmap Integration, Localization, Radius Claiming, Prefixes' update
- 4.1 'Faction Flags, Command System Revamp, More Config Options, More Commands & Many Fixes' update
- 4.2 'Placeholders, Map Command, Realms Layer, More Configuration & More' update
