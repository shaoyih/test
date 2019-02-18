# cs122b-winter19-team-92

## Optimazations

using batch insert and hash to optimaize speed

###batch insert
use preparstatement and batch insert to insert data into sql

###hash
- override hashcode in Movie in order to hash based on title,year,director
- get data from db and make a hashmap of the data to remove duplicates, so 
sql don't have to check for duplicate every times it inserts.


## design

Actor  -- parse Actor xml to get all the actor information (no duplicate on name), allow
            date of birth to be null, return a hashmap<Actor's name, Actor-class-object>

Genre--  parse genre from Main xml and database, return a 
            hashmap<Genre's name, Genre-id>, genre-id is the maximum id from the
            existing database

Movie-Star -- parse Cast xml and return a hashmap<Movie-id, list of Actor object>

Movie --    parse Main.xml and return a HashSet<Movie object>, hash on title,year,
                    director

### rule
    Movie don't allow duplicates between id or (title,year director)
    if any field of Movie is empty, it 's marked as inconsistent
    If a Star doesn't appear in Actor xml, it 's marked as inconsistent
    A actor can only appear once with the same name
    

