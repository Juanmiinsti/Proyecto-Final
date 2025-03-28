extends Node2D

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	var characterp1
	var characterp2
	var p1=$player1
	if CharacterDataSource.DBcharacters.size()==0:
		setDefaultStats(p1)
	else:
		characterp1=CharacterDataSource.DBcharacters[getcharid(p1.name)]
		print(characterp1.max_health)
		p1.HealthBar.max_value=characterp1.max_health
		p1.currentHealth=characterp1.max_health
		p1.HealthBar.value=characterp1.max_health
		p1.damage=characterp1.damage
	
	var p2=$player2
	
	if CharacterDataSource.DBcharacters.size()==0:
		setDefaultStats(p2)
	else:
		characterp2=CharacterDataSource.DBcharacters[getcharid(p2.name)]
		print(characterp2.max_health)
		p2.HealthBar.max_value=characterp2.max_health
		p2.currentHealth=characterp2.max_health
		p2.HealthBar.value=characterp2.max_health
		p2.damage=characterp2.damage

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass

func setDefaultStats(char:pCharacter):
	char.HealthBar.max_value=100
	char.currentHealth=100
	char.HealthBar.value=100
	char.damage=10
	

func getcharid (name:String):
	if name=='martial':
		return 0
	elif name=='huntress':
		return 1
	else:
		return 2	
