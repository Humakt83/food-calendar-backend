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
