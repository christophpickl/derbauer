
# TODO

* gold income DRASTISCH rauf, +30%
* mehr achievements:
    * achievements bringen gold
    * time-based achievements: alle 7 tage, 100 tage, 365 tage (=1jahr) ... free gold/food/land/buildings (when land available)
    
# UP NEXT

* attack target: split enemies into soldiers/knights, ... themselves too
* make window NOT resizable
* !! enhance release script: use src/releases/1.x.txt to generate github release text (keeping track manually)
* (4) ! MILITARY: getting attacked end of turn
* (2) gausche glocken kurve research+einbauen, see KRand (ad doku: log10, log, ...)
* (3) + TRADE: je mehr buy, desto teurer wirds, mit cooldown; vice versa sell
* ad HAPPENING
    * new happening: random people/army join; make happening#condition() .. check whether player good capacity/necessary features
    * increase gold bag happening amount (oder eh schon durch wealth gemacht?!)
    * when bad happening happens & karma is negative => karma++ (da schuld abgetan hat)
    - people die (murder/crime/krankheit)
    - free food
    - reisender gibt schriftrolle fuer free upgrade
    - some people/armies join

# Mit Rene

## LIL

* randomize food consumption and production
* castle attraktiver machen; guenstigere baukosten
* unbedingt TAX rate konzept einfuehren (modifier * people.count * random)
* nur "schoene" zahlen als limits verwenden, damit fuer den user klarer ersichtlich, also 100/250/500/1000/....
    - zb: castle braucht 50 leute, etc
* BUG: "people(s)" wird gerendert ad castle description
* buildings build by enter input number, also "16" zb eingeben, statt einzeln nur (von anfang an so machen)
* menu military text aendern: "whom attack" => "choose action (attack/hire)"
* trade land erst per feature enablebar; condition: 90% land used capacity
    
## MED

- render in top bar die tage als "1 year 112 days"
* upgrade / raufleveln!!! das motiviert :)
    * verschiedenste sachen rauf-level-bar
    * UPGRADES unendlich hoch upgradebar!
    * upgrade: food cap, people cap
        - durch das upgrade wird der building type changed: granary=>silo (quasi granary II); kostet mehr, bringt mehr
        - alle alten gebaeude typen werden upgraded: manuell/automatisch? for free/kostet (jedes einzelne building)?
        => ziel: anzahl an gebaeude unten halten, einfach halten
* ad baracke! nicht nur lagern, auch auswirkung auf produktion
* mini: view message nicht wirklich random machen; sondern auf daily base randomizen (auch mit cooldown arbeiten)
    - dont rewrite within same day; flackert komisch
* building.upkeep += people
    - render overview bar as "2 / 6 / 10", 2 used, 6 living, 10 max cap. (6-2 = 4 available)
    - oder doch nicht, da zu komplex?!? gold upkeep enough?
* ad reproduction rate:
    * death rate hoeher (vor allem on food shortage)
    * abhaengig machen von happiness (actions, social buildings) und karma
* split people rates into: A) birth rate und B) death rate
* introduce game preferences (persisted)
    - alert type: { off, beep, visual } // weil der beep sound kann super laestig werden!
    - visual != AlertGlasspane ... sondern eher nur ein kurzer flash
* IDEE: colored output

## BIG

* idea ad action: can hire manager delegate, which will for example: auto trade sell food if capacity at 80%
* ad military: wenn army hiren, dann werden sie zu "SHADOW PEOPLE"
    - kosten food, und alles was halt sonst people machen
    - bringen aber kein geld/tax income
* a la fugger: marry - birth - replay
    - sterbe rate von player steigt mit steigendem alter drastisch
    - auf wieviele jahre beschraenken? wie hundejahre * 7 zb... also nach 10 jahren ist todeswahrscheinlichkeit schon sehr hoch
* highscore
