function addIFrame(oNode, iCounter)
{
    var oIFrame = document.createElement("iframe");
    oIFrame.className = "InoculationFrame";
    oIFrame.id = "InoculateFrame" + iCounter;
    oNode.appendChild(oIFrame);
}

/**
* Ein kleiner Hack, damit das Menu ueber der SelectBox bleibt (IE-Problem)
**/
function inoculateMenuAgainstIE()
{
    var aoDiv;
    var oSubMenu;
    var iCount = 0;
    
    aoDiv = document.getElementsByTagName("DIV");
    for (var i = 0; i < aoDiv.length; i++)
    {
        if (aoDiv[i].className == "iceMnuBarSubMenu")
        {
            oSubMenu = aoDiv[i];
            oSubMenu.style.overflow = "hidden";
            addIFrame(oSubMenu, iCount);
            iCount++;
        }
    }
}

function checkInoculateFrames()
{
    var bInnoculated = false;
    var aoFrames;
    
    aoFrames = document.getElementsByTagName("iframe");
    for (var i = 0; i < aoFrames.length; i++)
    {
        if (aoFrames[i].className == "InoculationFrame")
        {
            bInnoculated = true;
            break;
        }
    }
    
    if (!bInnoculated)
        inoculateMenuAgainstIE();
}