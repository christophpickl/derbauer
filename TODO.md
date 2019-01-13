
# TODO

* keine 2 titles! zb im home screen, sondern nur einen!
* escape enabled (mit enter) irgendwo erklaeren
    - immer im prompt unten, rechtsbuendig
    - evtl zusaetzlich ESC unterstuetzen (?)
* throne room visitors growth DRASTISCH runter
    - max capacity depends on castle, aber niemals mehr als 1-2 dutzend
    - meist < 5, spaeter dann evtl sogar 10-15
* affordable amount schon in uebersicht anzeigen, nicht erst in sub view
* when warning overlay (gruenes glasspane) ++ go other screen fast => immediate! close (cancel the timeout)

# UP NEXT

* (6) MILITARY: attack target:
    - targets: wildlings, village, town, city, country, nation, empire
    * when attack won, there should be at least some loot, but never 0
    * loot DRASTISCH!!! erhoehen
        - depends on specific target
        - aufjedenfall so massig mehr GOLD rauskommen!! (land passt so halbwegs)
        - gibt als loot: gold/food/land spaeter fuer higher targets sogar: people/armies/upgrades?
* (4) ! MILITARY: getting attacked end of turn
* (2) gausche glocken kurve research+einbauen, see KRand
* (3) + TRADE: je mehr buy, desto teurer wirds, mit cooldown; vice versa sell
* ATTACK TARGETS:
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
    
* randomize food consumption
* castle attraktiver machen; guenstigere baukosten
* end attack view: layout armies lost so wie end turn report (genau sehen welche wieviel verloren)
* food.production *= random modifier
* gold income DRASTISCH rauf, +30%
    - unbedingt TAX rate konzept einfuehren (modifier * people.count * random)
* nur "schoene" zahlen als limits verwenden, damit fuer den user klarer ersichtlich, also 100/250/500/1000/....
    - zb: castle braucht 50 leute, etc
* BUG: "people(s)" wird gerendert ad castle description
* buildings build by enter input number, also "16" zb eingeben, statt einzeln nur (von anfang an so machen)
* menu military text aendern: "whom attack" => "choose action (attack/hire)"
* trade land erst per feature enablebar; condition: 90% land used capacity
    
## MED

* mehr achievements:
    * achievements bringen gold
    * time-based achievements: alle 7 tage, 100 tage, 365 tage (=1jahr) ... free gold/food/land/buildings (when land available)
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
* reproduction rate DRASTISCH runter
    - sowas sogar 40% runter
    * death rate hoeher (vor allem on food shortage)
    * abhaengig machen von happiness (actions, social buildings) und karma
* split people rates into: A) birth rate und B) death rate
* attack battle time ist "nearly" konstant (dauer: 2 bis max 10 sek)
    - am ende ein ease-out damit's spannend wird
* introduce game preferences (persisted)
    - alert type: { off, beep, visual } // weil der beep sound kann super laestig werden!
    - visual != AlertGlasspane ... sondern eher nur ein kurzer flash
* IDEE: colored output

## BIG

* ad military: wenn army hiren, dann werden sie zu "SHADOW PEOPLE"
    - kosten food, und alles was halt sonst people machen
    - bringen aber kein geld/tax income
* a la fugger: marry - birth - replay
    - sterbe rate von player steigt mit steigendem alter drastisch
    - auf wieviele jahre beschraenken? wie hundejahre * 7 zb... also nach 10 jahren ist todeswahrscheinlichkeit schon sehr hoch
* highscore
