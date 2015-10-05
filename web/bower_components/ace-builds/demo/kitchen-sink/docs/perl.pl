#!/usr/bin/perl
#
# Licensed to STRATIO (C) under one or more contributor license agreements.
# See the NOTICE file distributed with this work for additional information
# regarding copyright ownership.  The STRATIO (C) licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

=begin
 perl example code for Ace
=cut

use strict;
use warnings;
my $num_primes = 0;
my @primes;

# Put 2 as the first prime so we won't have an empty array
$primes[$num_primes] = 2;
$num_primes++;

MAIN_LOOP:
for my $number_to_check (3 .. 200)
{
    for my $p (0 .. ($num_primes-1))
    {
        if ($number_to_check % $primes[$p] == 0)
        {
            next MAIN_LOOP;
        }
    }

    # If we reached this point it means $number_to_check is not
    # divisable by any prime number that came before it.
    $primes[$num_primes] = $number_to_check;
    $num_primes++;
}

for my $p (0 .. ($num_primes-1))
{
    print $primes[$p], ", ";
}
print "\n";

