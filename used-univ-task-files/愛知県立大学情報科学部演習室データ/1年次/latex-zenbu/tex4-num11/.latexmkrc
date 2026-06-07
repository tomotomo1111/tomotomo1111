#!/usr/bin/env perl
$latex         = 'platex -synctex=1';
$latex_silent  = 'platex -synctex=1 -interaction=batchmode';
$bibtex        = 'bibtex';
$dvipdf        = 'dvipdfmx %O -o %D %S';
$makeindex     = 'mendex %O -o %D %S';
$max_repeat    = 5;

$pdf_mode      = 3;          # generates PDF
$pdf_previewer = "okular";   # Use Okular as a previewer
