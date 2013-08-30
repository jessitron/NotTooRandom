#!/bin/sh ruby

`say Oh!`

result = if system "gradle test"
  'Fabulous'
else
  '-v Agnes Something borked'
end

`say #{result}`

exit 0
