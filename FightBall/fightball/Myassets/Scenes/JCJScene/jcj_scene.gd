extends Node2D

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	
	var p1=$player1
	var characterp1=CharacterDataSource.DBcharacters[getcharid(p1.name)]
	print(characterp1.max_health)
	p1.HealthBar.max_value=characterp1.max_health
	p1.currentHealth=characterp1.max_health
	p1.HealthBar.value=characterp1.max_health
	
	var p2=$player2
	
	var characterp2=CharacterDataSource.DBcharacters[getcharid(p2.name)]
	print(characterp2.max_health)
	p2.HealthBar.max_value=characterp2.max_health
	p2.currentHealth=characterp2.max_health
	p2.HealthBar.value=characterp2.max_health

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass

func getcharid (name:String):
	if name=='martial':
		return 0
	elif name=='huntress':
		return 1
	else:
		return 2	
