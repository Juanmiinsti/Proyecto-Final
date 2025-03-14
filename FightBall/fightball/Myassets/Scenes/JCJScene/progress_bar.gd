extends ProgressBar

var player=player2

func _ready():
	player=$Player1
	player.healthChanged.connect(update_health_bar())
	update_health_bar()  # Actualiza la barra de salud al inicio

func update_health_bar():
	value = player.currentHealth *100/player.maxHealth
	
