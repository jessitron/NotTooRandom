# A sample Guardfile
# More info at https://github.com/guard/guard#readme

guard :shell do
  watch(%r{^src/*/.+\.scala$}) { `ruby run_tests.rb` }
end

