# ircddb-tailer

This tiny tool keeps track of my friends using the D-Star amateur radio network.

The Scala Play app exposes 3 urls:

- https://tranquil-bastion-39552.herokuapp.com/poll
- https://tranquil-bastion-39552.herokuapp.com/stats
- https://tranquil-bastion-39552.herokuapp.com/dump


## Heroku configuration

### Clock
 - It is possible to schedule a recurring job on Heroku - run `heroku ps:scale clock=1`
