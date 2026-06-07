ren6 xs = t == xs
  where
    t =  [ x | x <- xs, mod x 3 == 1]

ren7 xs = elem 'a' xs

ren8 a b = floor(sum [ t**n | n <- [1..3]])
  where
    t = a-b

ren9 n = reverse t == t
  where
    t = show n

ren10 x a b = if l > r then 'A' else 'B'
  where
    r = abs (a - x)
    l = abs (b - x)

ren11 x t = x - t

ren12 n = elem '9' (show n)


