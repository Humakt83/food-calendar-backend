-- :name dishes-all :? :*
-- :doc select all dishes with all the attributes
SELECT * FROM dishes

-- :name dishes
-- :command :query
-- :result n
-- :doc select all with cols defined by {:cols [<col_name>...]}
SELECT :i*:cols FROM dishes

-- Note the terse style below
-- ":command :query" -> ":?"
-- ":result n" -> ":*" 

-- :name dishes-by-dishtype :? :*
-- :doc get dishes by dishtype
SELECT * FROM dishes
 WHERE dishtype = :dishtype

-- :name dishes-by-name :? :*
-- :doc get dishes by name
SELECT * FROM dishes
 WHERE name = :name

-- :name new-dishes :! :n
-- :doc insert new dishes
INSERT INTO dishes (name, dishtype)  
VALUES (:name, :dishtype)

-- :name food-calendar-day-all :? :*
-- :doc select all food-calendar-day with all the attributes
SELECT fcd.date, fds.section, d.name FROM foodCalendarDay fcd, foodDaySection fds, dishes d
 WHERE fcd.id = fds.foodCalendarDay AND fds.dish = d.id;

-- :name new-food-calendar-day :<!
-- :doc insert new food-calendar-day
INSERT INTO foodCalendarDay (date)  
VALUES (:date) returning id

-- :name new-food-day-section :! :n
-- :doc insert new food-day-section
INSERT INTO foodDaySection (dish, foodCalendarDay, section)
VALUES (:dish, :foodCalendarDay, :section)

-- :name food-calendar-day-by-date :? :*
-- :doc get food-calendar-day by date
SELECT fcd.date, fds.section, d.name FROM foodCalendarDay fcd, foodDaySection fds, dishes d
 WHERE fcd.id = fds.foodCalendarDay AND fds.dish = d.id AND fcd.date >= :date AND fcd.date < :nextday 
 
-- :name remove-food-from-day :! :n
-- :doc remove food from day by food and date
DELETE FROM foodDaySection fds
 	USING
 		foodCalendarDay fcd,
 		dishes d
 WHERE fcd.id = fds.foodCalendarDay AND fds.dish = d.id AND d.name = :dish AND fcd.date >= :date AND fcd.date < :nextday
 
 