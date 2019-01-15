
# UP NEXT

* when bad happening happens & karma is negative => karma++ (da schuld abgetan hat)
* attack is a one-turn thing only: send armies, on endturn see the battle; disable menu item "attack" when already attack ordered 
* make window NOT resizable
* (4) ! MILITARY: getting attacked end of turn
* (2) gausche glocken kurve research+einbauen, see KRand (ad doku: log10, log, ...)
* (3) + TRADE: je mehr buy, desto teurer wirds, mit cooldown; vice versa sell
* !! enhance release script: use src/releases/1.x.txt to generate github release text (keeping track manually)

# LATER

* attack target: split enemies into soldiers/knights
* TAX rate konzept einfuehren (modifier * people.count * random)
* nur "schoene" zahlen als limits verwenden, damit fuer den user klarer ersichtlich, also 100/250/500/1000/....
    - zb: castle braucht 50 leute, etc
* randomize food consumption and production

# OTHER

- render in top bar die tage als "1 year 112 days"
* upgrade / raufleveln!!! das motiviert :)
    * verschiedenste sachen rauf-level-bar
    * UPGRADES unendlich hoch upgradebar!
    * upgrade: food cap, people cap
        - durch das upgrade wird der building type changed: granary=>silo (quasi granary II); kostet mehr, bringt mehr
        - alle alten gebaeude typen werden upgraded: manuell/automatisch? for free/kostet (jedes einzelne building)?
        => ziel: anzahl an gebaeude unten halten, einfach halten
* ad baracke! nicht nur lagern, auch auswirkung auf produktion
* ad reproduction rate:
    * death rate hoeher (vor allem on food shortage)
    * abhaengig machen von happiness (actions, social buildings) und karma
* split people rates into: A) birth rate und B) death rate
* introduce game preferences (persisted)
    - alert type: { off, beep, visual } // weil der beep sound kann super laestig werden!
    - visual != AlertGlasspane ... sondern eher nur ein kurzer flash
* ad military: wenn army hiren, dann werden sie zu "SHADOW PEOPLE"
    - kosten food, und alles was halt sonst people machen
    - bringen aber kein geld/tax income
