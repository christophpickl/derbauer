# with seraphine

* statt "choose" spezifischer titel
* feedback bei aktion (zb gebaeude gebaut)
* cancel aktion (mit gleich enter) irgendwo erklaeren
* gebaeude abreissen: kostet gold

# major

* seperate screen when notification (good news)
* (1) rename "people" => "peasants"
* (2) upcome costs (some buildings, all military)
* (3) MILITARY: chooose how many troops to send
* (2) if food/people are over capacity => decrease it slowly!
* rename to Army (not military)

# minor

* create new kpot module, containing /bin kts scripts (use gradle to download and unzip in /bin)
* (4) ! MILIATRY: getting attacked end of turn
* (3) + TRADE: je mehr buy, desto teurer wirds, mit cooldown; vice versa sell
* (6) MILIATRY: attack target: { wildlings, the empire }
* (1) ? build multiple buildings at once?
* (1) - HAPPENING: make bag sizes depend on current Model resource
* (2) - VISITOR: accumulate probability; dynamic reward calculation (depends on progress/availability)
* if too low income for upcome costs: soldiers slowly run away, buildings will get abandoned
* scout: is a (non-army) military unit; to see when the next attack is approaching

# ideas

* concept of karma (good/bad decisions); aka "global luck" (e.g. for good/bad happenings, random calc)
* concept of wealth: count all land/building/units
* mittelalterliche BG music
* very detailed end-turn report (was weg/was dazu, warum/woher)
    - aus verschiedene bereiche/sectioned (economic, military, ...)
    - gibt stats aus ueber production/upcome/.../achievements/...

## I/O

* immediate print result of action; e.g.: bought farm => total food production increase from X to Y
* zu jeder option immer dazuschreiben ihren effekt
* print header ascii art for each section (build/trade/military/upgrade)
* show options which are not available yet "x)"

## new concepts

* new "resource": 
    - happyness/public opinion
    - stone/wood
* religion
* change laws
    - taxes (gambling, banking, land, military)
    - +gold prod; -happyness/people prod
    - feature enabled when ... upgrade "politics" + building "tax office"
* diplomacy
    * mit verschiedene voelker, relationship metrik
    * actions: buy/sell troops, handeln/schenken/verlangen, buendnis, ...
    * other nations: info about resources, army, ....
        - diplomacy, spy, war
        - trade agreement, send gift, hire troops, alliance
        - happening: get free stuff from other; get requested to donate/help/escort (borrow miliatry units)
* bank
    - deposit/withdray money (earn interest), take loan
