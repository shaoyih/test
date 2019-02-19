USE `moviedb`;
DROP PROCEDURE IF EXISTS `add_movie`;
DELIMITER $$
CREATE PROCEDURE `add_movie` (
in title varchar(100),
in year integer,
in director varchar(100),
in star varchar(100),
in genre varchar(32),
out message varchar(100),
out statuss varchar(100))

BEGIN
	DECLARE MovieId varchar(10);
    DECLARE buffer integer;
    
    DECLARE GenreId integer;
    DECLARE StarId varchar(10);
    
    
    
	IF (star is NULL) OR (length(star)<=0) or (title is NULL) OR (length(title)<=0) or (year is NULL) OR (length(year)!=4) or (director is NULL) OR (length(director)<=0) or (genre is NULL) OR (length(genre)<=0) THEN
		BEGIN
			SELECT 'fail' INTO statuss;
			IF (star is NULL) OR (length(star)<=0)
				THEN SELECT 'star' INTO message;
			END IF;
			IF(title is NULL) OR (length(title)<=0)
				THEN SELECT 'title' INTO message;
			END IF;
			IF(year is NULL) OR (length(year)!=4)
				THEN SELECT 'year' INTO message;
                
			END IF;
			IF(director is NULL) OR (length(director)<=0)
				THEN SELECT 'director' INTO message;
			END IF;
			IF (genre is NULL) OR (length(genre)<=0)
				THEN SELECT 'genre' INTO message;
			END IF;
		END;
    ELSE
		BEGIN
			SELECT id into MovieId FROM movies as m WHERE m.title=title and m.year=year and m.director=director;
            IF ((MovieId IS NULL) or (length(MovieId)<=0))
            Then
				BEGIN
                
                SELECT MAX(id) INTO MovieId FROM movies WHERE id LIKE 'tt______%';
				SELECT CAST(SUBSTRING(MovieId,3) AS unsigned integer)+1 into buffer;
				SELECT CONCAT(SUBSTRING(MovieId,1,2),CAST(buffer AS CHAR)) INTO MovieId;
                INSERT INTO movies (id,title,year,director) values(MovieId,title,year,director); 
                INSERT INTO ratings VALUES(MovieId,0,0);
                
				SELECT id INTO GenreId FROM genres WHERE name=genre;
				IF (GenreId is NULL)
					THEN 
						SELECT MAX(id)+1 INTO GenreId FROM genres;
						
						INSERT INTO genres (id,name) values (GenreId,genre);
				END IF;
				INSERT INTO genres_in_movies (genreId,movieId) values (GenreId,MovieId);
				
				SELECT id INTO StarId FROM stars WHERE name=star;
				IF (StarId is NULL)
					THEN 
						SELECT MAX(id) INTO StarId FROM stars;
						SELECT CAST(SUBSTRING(StarId,3) AS unsigned integer)+1 into buffer;
						SELECT CONCAT(SUBSTRING(StarId,1,2),CAST(buffer AS CHAR)) INTO StarId;
						
						INSERT INTO stars (id,name) values (StarId,star); 
				END IF;
				INSERT INTO stars_in_movies (StarId,movieId) values (StarId,MovieId); 
                SELECT 'success' INTO statuss;
                END;
			ELSE
				SELECT 'exist' INTO statuss;
            END IF;
			
        END;
	END IF;
	 
END $$
year
DELIMITER ;