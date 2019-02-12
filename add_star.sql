USE `moviedb`;
DROP PROCEDURE IF EXISTS `add_star`;
DELIMITER $$
CREATE PROCEDURE `add_star` (
in star_name varchar(100),
in star_birth integer,
out message varchar(100))

BEGIN
	DECLARE maxId varchar(10);
    DECLARE intId integer;
    DECLARE back varchar(10);
     
	IF (star_name is NULL) OR (length(star_name)<=0)
	THEN SELECT 'fail' INTO message;
    ELSE
		SELECT MAX(id) INTO maxId FROM stars;
        SELECT CAST(SUBSTRING(maxID,3,7) AS unsigned)+1 into intId;
        SELECT CONCAT(SUBSTRING(maxId,1,2),CAST(intId AS CHAR)) INTO maxId;
        INSERT INTO stars (id,name,birthYear) VALUES(maxId,star_name,star_birth);
		SELECT 'success' INTO message;
	END IF;
	 
END $$

DELIMITER ;