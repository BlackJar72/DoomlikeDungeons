This folder is for files partially changing themes by adding mobs or blocks.  It will add any mob or block lists and
add them to the named theme.  This is too allow things like mobs to be added to dungeon themes by multiple packs
without the changes made by one data pack overwriting another.  For example, data packs could be made for two different
mobs mods to add their own mobs, and both or neither could be used.

Files in this folder will not be treated as themes, that is, they will not create new themes.  If you want to create
a new theme, or modify and existing them extensively, use the themes folder, not this one.  Editing themes in the
themes folder is also required to remove blocks or mobs, as files here only add them.

The following fields from themes can be have data appended to them:

    Block fields: walls, floors, ceilings, fencing, liquid, pillarBlock, caveblock, air
    Mob fields: commonMobs, hardMobs, bruteMobs, eliteMobs, bossMobs

See the theme files for description of the categories and examples of what usually goes there.

These files also need the name of the theme to modify, formated as "dldungeonsjbg:theme_name" (for example, the theme
in common.cfg will be dldungeonsjbg:common).  You can list more than one theme if you like.  This MUST be at the top
of the file, or it will not work.  If anything other than comments (starting with #) is found it will move on and
start adding content to whatever themes have been listed so far (even if that mean none).

The name of the files here could be anything but MUST end in ".cfg" and should be more than just the theme names, so
that files from different data packs will not overwrite each other.  For example, common_my_cool_mod to add mobs /
blocks from My Cool Mod to the common theme.

************************************************************************************************************************
EXAMPLE BELOW -- files in this folder should look like this; you can include any of the fields list above.
                 This would add the listed blocks and mobs to both the common and urban theme.
                 The contents below would be place in a file called something like cool_mod_stuff.cfg
************************************************************************************************************************

themes = dldungeonsjbg:common, dlddungeonsjbg:urban

commonMobs =  cool_mod:cool_mob
bossMobs = cool_mod:cool_boss

pillarBlock =  cool_mod:cool_block
