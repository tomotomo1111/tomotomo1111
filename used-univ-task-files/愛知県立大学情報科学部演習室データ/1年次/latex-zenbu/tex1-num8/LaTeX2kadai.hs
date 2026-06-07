f::Int->Int->Int->Int

f x w k | k == 0           = 1
        | k `mod` 2 == 0   = f'
	| otherwise        = x * f' `mod` w
	where f' = f (x * x `mod` w) w (k `div` 2)