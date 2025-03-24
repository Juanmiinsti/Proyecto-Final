extends Node2D

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	var p1=$player1
	var characterp1=CharacterDataSource.DBcharacters[getcharid(p1.name)]
	p1.p1HealthBar.max_value=characterp1.max_health
	p1.p1HealthBar.value=characterp1.max_health
	
	var char2=$player2

	char2.p1HealthBar.max_value=1
	char2.p1HealthBar.value=1

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass

func getcharid (name:String):
	if name=='martial':
		return 0
