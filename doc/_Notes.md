
* people = peasants = villagers
* army = military unit = militia

# Other similar games

* Warsim
    * very big, nice game (5 USD, win only)
    * Website: https://huw2k8.itch.io/warsim
    * Gameplay: https://www.youtube.com/watch?v=xcnHcXXRzlI
* the dark ages
    * very tiny game, python
    * https://github.com/haydenhughes/the-dark-ages
* open dominion
    * average size, PHP
    * https://github.com/WaveHack/OpenDominion
* hamurabi
    * lil game, JS
    * https://github.com/skypanther/Hamurabi/blob/master/Resources/common/hamurabi.js#L62
* Die Fugger II

## collections

* https://github.com/search?q=text+strategy+game
* https://www.ifarchive.org/indexes/if-archiveXgames.html

# Nope

* ~~select race at beginning~~
* ~~select begin difficulty at startup~~

# Fancy ideas

* multiplayer via network
* time-based, each 1sec = 1 tick

# Normal Distribution

* "uniform distribution": complete random; "normal distribution": gaussian
* java.util.Random.nextGaussian() * deviation + mean
* or use apache math3: https://commons.apache.org/proper/commons-math/userguide/distribution.html
* needed as well for derbauer:
    - random without replacement
    - cool down: number of tries since last success increases probability
* params:
    1. erwartungswert/mean mu: wo wird maximum sein auf x-achse
    1. variance^2: standardabweichung, wie weit weg von mean, >0
* TODO: wie kann ich schauen, dass ab bestimmte grenze werte nur noch 0 sind?!
* related to: binominal distribution/coefficient
* help:
    - basic math: https://matheguru.com/stochastik/normalverteilung.html
    - for game dev: https://www.alanzucconi.com/2015/09/09/understanding-the-gaussian-distribution/
