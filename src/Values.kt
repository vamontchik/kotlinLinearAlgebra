enum class Direction { UP, LEFT, DOWN, RIGHT }

data class Values(var x: Int, var y: Int,
                  var leftToWalk: Int, var totalLength: Int, var timeToIncrease: Boolean,
                  var count: Int,
                  var direction: Direction)