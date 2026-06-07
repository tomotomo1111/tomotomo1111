double :: Int -> Int
double x = x + x　--二倍
quadruple x = double (double x) --四倍
legs c t = 4 * c + 2 * t --足の数
para r1 r2 = (r1 * r2) / (r1 + r2) {-電気抵抗の合成-}
mid x = mod (div x 100) 100      --六桁の数字の中2文字
myLast1 xs = xs !! (length xs - 1)
myLast2 xs = head (reverse xs)

myinit1 xs = take (length xs - 1) xs
myinit2 xs = reverse (tail (reverse xs))

isLower1 c = 'a' <= c && c <= 'z'
isLower2 c = elem c "abcdefghijklmnopqrstuvwxyz"

abc252a n ="abcdefghijklmnopqrstuvwxyz" !! (n - 97)

myAbs n = if n >= 0 then n else -n
mySignum n = if n > 0 then 1 else (if n < 0 then -1 else 0)
foo x = (if x == 3 then "3" else "not three")
bar x = (if x == 3 then "3" else "not three")
sen h = take h ["a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"]

 
abc251a s =  
  if length s == 1 then s ++ s ++ s ++ s ++ s ++ s
else
  if length s == 2 then s ++ s ++ s else
  s ++ s
abc251a1 s = take 6 (s ++ s ++ s ++ s ++ s ++ s) 
abc251a2 s = take 6 (cycle s)

triArea a b c = sqrt (((a+b+c)/2)*((a+b+c)/2-a)*((a+b+c)/2-b)*((a+b+c)/2-c))

triArea2 a b c = sqrt (s * (s-a) * (s-b) * (s-c))
  where
    s = (a+b+c) / 2  {-インデントtabは使えない-}

myAbs3 n 
  |n >= 0 = n
  |otherwise = -n

score x y 
  |x^2 + y^2 <= 3^2 = 50
  |x^2 + y^2 <= 5^2 = 10
  |otherwise = 0
score1 x y
  |d2 <= 3^2 = 50
  |d2 <= 5^2 = 10
  |otherwise = 0
  where d2 = x^2 + y^2

--(x + y| x <- [100,155,265],y <- [1.2.3.4])--
--isSquare : Int -> Bool--
isSquare n = elem n [1,4,9]

isSquare n = n == 1 || n == 4 || n == 9

{-isSquare n
  |n == 1 = true
  |n == 4 = True
  |n == 9 = True 
  |otherwise = False
-}
isSquare 1 = True
isSquare 4 = True
isSquare 9 = True
isSquare _ = False --wildcard

and2 a b = if a then (if b then True else False) else False

and2 True True = True
and2 _ _= False

park n a b = if n * a < b then n * a else b
place s = f (s !! 0) + f (s !! 1) + f (s !! 2)
  where 
    f c = if c == '1' then 1 else 0
round1 a b = div (a + b + 1) 2
libra a b c d =
  if l > r then "left" else
  if l < r then "right" else
  "balanced"
  where l = a + b
        r = c + d
newyear m = 48 - m
already s = "2018" ++ (drop 4 s)
product1 a b = if odd (a * b) then "odd" else "even"
buyingsweet x a b = mod (x - a) b
infinitecoins n a = (mod n 500) <= a 
group n = div n 3

not :: Bool -> Bool
not b = if b then False else True 

not b
  | b = False 
  | otherwise = True

not b
  | b == True = False 
  | otherwise = True 

and3 :: Bool -> Bool -> Bool 
and3 b1 b2 = if b1 then (if b2 then True else False) else False

and3 b1 b2
  | b1,b2 = True 
  | otherwise = False


and3 True  True  = True
and3 True  False = False
and3 False True  = False 
and3 False False = False 

and3 True True   = True
and3 _    _      = False

and3 True  b2 = b2
and3 False _  = False

factor n = [k|k<-[1..n],mod n k == 0]
prime1 n = factor n == [1,n]


--succAll ns = [succ n | n <- ns]

double1 x = x + x
doubleAll ns = [double n | n <- ns]

--notAll bs = [not b | b <- bs]

myMap f xs = [f x | x <- xs]

evenOnly ns = [n | n <- ns, even n]
positiveOnly ns = [n | n <- ns, n > 0]
lennOnly n ss = [s | s <- ss, length s == n]

myFilter p xs = [x | x <- xs, p x]

--evenOnly ns === myFilter even ns
--positiveOnly ns ~~~ myFilter (\n -> n > 0) ns === myFilter (0 < n)

len3Only ss = myFilter p ss
  where
    p s = length s == 3

factorial 0 = 1
factorial n = n * (factorial (n-1))

composite f g x = f . (g . x)
--composite f g === f . g
--composite f g = (\x -> f (g x))

regularTriangle = [(0,0), (2,0), (1,sqrt 3)]
shift dx dy (x,y) = (x+dx, y+dy)
shiftx1y3 = map (shift 1 3) regularTriangle
find k t = head [v | (k1,v) <- t, k == k1] 
type Book = ( String , String , Int )

type Circle = Double
areaCircle r = div (floor (10 * pi * r * r)) 10
type Rect = (Double, Double)
areaRect (w,h) = w*h

data Shape = Circle Double | Rect Double Double
aCircle = Circle 2 :: Shape
aRect = Rect 3 3 :: Shape

{-
data Maybe a = Nothing 
             | Just a

safeDiv _ 0 = Nothing
safeDiv m n = Just (div m n)

safeHead [] = Nothing
safeHead (x:_) = Just x 
-}


data Tree a = Leaf | Node (Tree a) a (Tree a)
singleton :: a -> Tree a
singleton x = Node Leaf x Leaf

{-
t :: Tree Int
t = Node 1 5 r
  where
    l = Node (singleton 1) 3 (singleton 4)
    r = Node (singleton 6) 7 (singleton 9)

occurs :: Eq a => a -> Tree a -> Bool
occurs x Leaf         = False
occurs x (Node l y r) = x == y || occurs x l || occurs x r

flatten :: Tree a - > [a]
flatten Leaf = []
flatten (Node l x r) = flatten l ++ [x] ++ flatten r
-}

{-
data Expr = Val Int | Add Expr Expr

toString :: Expr -> String
toString (Val n) = show n
toString (Add e1 e2) = concat ["(",toString e1,"+",toString e2,")"]

eval :: Expr -> Int
eval (Val n) = n
eval (Add e1 d2) = eval e1 + eval e2
-}