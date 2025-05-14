extends Node2D


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	var characterp1
	var p1 = $NavigationRegion2D/player1
	p1.character=Persistence.character
	# Configuración de estadísticas del jugador 1
	if CharacterDataSource.DBcharacters.size() == 0:
		# Si no hay personajes en la DB, usa valores por defecto
		setDefaultStats(p1)
	else:
		# Obtiene los datos del personaje del jugador 1 desde la DB
		characterp1 = CharacterDataSource.DBcharacters[(getcharid(Persistence.charname1))-1]
		print(characterp1.max_health)
		# Configura la barra de vida y estadísticas según los datos del personaje
		p1.HealthBar.max_value = characterp1.max_health
		p1.currentHealth = characterp1.max_health
		p1.HealthBar.value = characterp1.max_health
		p1.damage = characterp1.damage


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass
	
func setDefaultStats(char: pCharacter):
	char.HealthBar.max_value = 100
	char.currentHealth = 100
	char.HealthBar.value = 100
	char.damage = 10
	
func getcharid(name: String):
	if name == 'Huntress':
		return 1
	elif name == 'Martial':
		return 2
	else:
		return 3    	
