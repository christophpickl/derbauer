
# TODO

* new happening: random people/army join; make happening#condition() .. check whether player good capacity/necessary features
* (6) MILIATRY: attack target: { wildlings, the empire }
* (4) ! MILIATRY: getting attacked end of turn
* (2) gausche glocken kurve research+einbauen, see KRand

# UP NEXT

* (3) + TRADE: je mehr buy, desto teurer wirds, mit cooldown; vice versa sell
* when attack won, there should be at least some loot, but never 0

# Mit Rene

## brainstorming

* komplexe charaktere wahl (rasse, geschlecht, ...)
    - effects: military, forschung
* upgrade / raufleveln!!!
    - verschiedenste sachen rauf-level-bar
* Achievements
* epochen; zb: renessaince, ...
* "esoterische perspektive" => alles hat auswirkung aufeinander
* worker types
    - ingenieur, schmied, heiler
* highscore

* ad military:
    - waffenarten: schwert, axt, ...
    - typen: nahkampf, fernkampf, magisch
    - special resources: metal, pferde
    - spione: enemy stats reveal
    - on attacks:
        * weakened for some turns, after military interaction
* food typen:
    - fleisch/fisch, apfel
* building resource types (tlw auch fuer military):
    - stein, holz, eisen
* ad baracke! nicht nur lagern, auch auswirkung auf produktion
* buildings bspe: kaserne, schmiede, universitaet, farm, castle


## protocol

* ad render: kosten anzeige auch mit k/m/g ... suffix amount
* ad military: wenn army hiren, dann werden sie zu "SHADOW PEOPLE"
    - kosten food, und alles was halt sonst people machen
    - bringen aber kein geld/tax income
* BUG: trade view zeigt ohne modifier an, zb statt 46 gold die basis von 50 gold (trade land)
* mini: view message nicht wirklich random machen; sondern auf daily base randomizen (auch mit cooldown arbeiten)
    - dont rewrite within same day; flackert komisch
* affordable amount schon in uebersicht anzeigen, nicht erst in sub view
* present amount (a la "you've got") nicht drunter als eigene sektion, sondern selbe zeile mit item anzeigen
    zb: "[2] Farm (17) ... 14 Gold, 2 Land (Food production +2)"
* randomize food consumption
* keine 2 titles! zb im home screen, sondern nur einen!
* render resource.label.capitalize (in top overview bar)
* castle attraktiver machen; guenstigere baukosten
* escape enabled (mit enter) irgendwo erklaeren
    - immer im prompt unten, rechtsbuendig
    - evtl zusaetzlich ESC unterstuetzen (?)
* throne room visitors growth DRASTISCH runter
    - max capacity depends on castle, aber niemals mehr als 1-2 dutzend
    - meist < 5, spaeter dann evtl sogar 10-15
* building.upkeep += people
    - render overview bar as "2 / 6 / 10", 2 used, 6 living, 10 max cap. (6-2 = 4 available)
    - oder doch nicht, da zu komplex?!? gold upkeep enough?
* reproduction rate DRASTISCH runter
    - sowas sogar 40% runter
    * death rate hoeher (vor allem on food shortage)
    * abhaengig machen von happiness (actions, social buildings) und karma
* trade land erst per feature enablebar; condition: 90% land used capacity
* when bad happening happens & karma is negative => karma++ (da schuld abgetan hat)
* when warning overlay (gruenes glasspane) ++ go other screen fast => immediate! close (cancel the timeout)
* IDEE: colored output
* end attack view: layout armies lost so wie end turn report (genau sehen welche wieviel verloren)
* new happening:
    - people die (murder/crime/krankheit)
    - free food
    - reisender gibt schriftrolle fuer free upgrade
    - some people/armies join
* split people rates into: A) birth rate und B) death rate
* nur "schoene" zahlen als limits verwenden, damit fuer den user klarer ersichtlich, also 100/250/500/1000/....
    - zb: castle braucht 50 leute, etc
* food.production *= random modifier
* gold income DRASTISCH rauf, +30%
    - unbedingt TAX rate konzept einfuehren (modifier * people.count * random)
* attack loot DRASTISCH!!! erhoehen
    - depends on specific target
    - aufjedenfall so massig mehr GOLD rauskommen!! (land passt so halbwegs)
    - gibt als loot: gold/food/land spaeter fuer higher targets sogar: people/armies/upgrades?
* BUG: "people(s)" wird gerendert ad castle description
* increase gold bag happening amount (oder eh schon durch wealth gemacht?!)
* upgrade: food cap, people cap
    - durch das upgrade wird der building type changed: granary=>silo (quasi granary II); kostet mehr, bringt mehr
    - alle alten gebaeude typen werden upgraded: manuell/automatisch? for free/kostet (jedes einzelne building)?
    => ziel: anzahl an gebaeude unten halten, einfach halten
* buildings build by enter input number, also "16" zb eingeben, statt einzeln nur (von anfang an so machen)
* menu military text aendern: "whom attack" => "choose action (attack/hire)"
* UPGRADES unendlich hoch upgradebar!
* time-based achievements: alle 7 tage, 100 tage, 365 tage (=1jahr) ... free gold/food/land/buildings (when land available)
    - render in top bar die tage als "1 year 112 days"
* a la fugger: marry - birth - replay
    - sterbe rate von player steigt mit steigendem alter drastisch
    - auf wieviele jahre beschraenken? wie hundejahre * 7 zb... also nach 10 jahren ist todeswahrscheinlichkeit schon sehr hoch
* attack battle time ist "nearly" konstant (dauer: 2 bis max 10 sek)
    - am ende ein ease-out damit's spannend wird
* achievements bringen gold
* introduce game preferences (persisted)
    - alert type: { off, beep, visual } // weil der beep sound kann super laestig werden!
    - visual != AlertGlasspane ... sondern eher nur ein kurzer flash
